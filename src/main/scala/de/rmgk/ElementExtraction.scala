package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Measurement, Mode, OutputTimeUnit, Scope, State, Warmup}
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class ElementExtraction {

  var someInt: Option[Int] = Some(1)
  var j: Int = 10


  @Benchmark
  def foreach(bh: Blackhole) = {
    someInt.foreach(i => bh.consume(i + j))
  }

  @Benchmark
  def newFunction(bh: Blackhole) = {
    someInt.foreach(new Function[Int, Unit] {
      override def apply(v1: Int): Unit = bh.consume(v1 + j)
    })
  }

  @Benchmark
  def ifAccessor(bh: Blackhole) = {
    if (someInt.isDefined) bh.consume(someInt.get + j)
  }

  @Benchmark
  def pattern(bh: Blackhole) = someInt match {
    case Some(i) => bh.consume(i + j)
    case None => ()
  }
}
