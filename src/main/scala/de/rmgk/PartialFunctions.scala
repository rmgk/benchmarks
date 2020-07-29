package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{
  Benchmark, BenchmarkMode, Fork, Measurement, Mode, OutputTimeUnit, Scope, State, Warmup
}
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class PartialFunctions {

  val ab = List.tabulate(10)(identity)

  val work = 100L

  @Benchmark
  def usePartialFunction(bh: Blackhole) = {
    ab.collect {
      case x if { Blackhole.consumeCPU(work); true } => x
    }
  }

  @Benchmark
  def useFlatmap(bh: Blackhole) = {
    ab.flatMap { x =>
      if ({ Blackhole.consumeCPU(work); true }) Some(x)
      else None
    }
  }
}
