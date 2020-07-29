package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{
  Benchmark, BenchmarkMode, Fork, Measurement, Mode, OutputTimeUnit, Scope, State, Warmup
}

import scala.util.DynamicVariable

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class DeclarationLocationName {

  def declarationLocationName(): String =
    if (dynamicNameVar.value.nonEmpty) dynamicNameVar.value
    else {
      val trace = Thread.currentThread().getStackTrace
      var i     = 0
      while (
        trace(i).toString.startsWith("scala.") || trace(i).toString.startsWith("java.") ||
        (trace(i).toString.startsWith("de.rmgk") && !trace(i).toString.startsWith("de.rmgk.test"))
      ) i += 1

      s"${trace(i).getFileName}(${trace(i).getLineNumber})"
    }

  val dynamicNameVar = new DynamicVariable("")

  @Benchmark
  def gettingLocationNames(): String = {
    declarationLocationName()
  }

  @Benchmark
  def currentStackTrace() = Thread.currentThread().getStackTrace
}
