package de.rmgk

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{ThreadLocalRandom, TimeUnit}

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Group, GroupThreads, Measurement, Mode, OutputTimeUnit, Scope, Setup, State, Warmup}
import org.openjdk.jmh.infra.{BenchmarkParams, ThreadParams}

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class Volatiles() {

  var field: Array[AtomicReference[Object]] = _

  @Setup
  def setup(benchmarkParams: BenchmarkParams) = {
    field = Array.fill(benchmarkParams.getThreads)(new AtomicReference[Object](new Object))
  }

  @Benchmark
  @Group("volatile")
  @GroupThreads(3)
  def read( params: ThreadParams) = {
    field(params.getThreadIndex).get()
  }

  @Benchmark
  @Group("volatile")
  @GroupThreads(1)
  def write(params: ThreadParams) = {
    field(params.getThreadIndex).getAndSet(params)
  }

//  @Benchmark
//  def update() = {
//    field = field + 1
//  }
//
//  val ar: AtomicReference[Object] = new AtomicReference[Object](new Object)
//
//  @Benchmark
//  //  @Group("volatile")
//  //  @GroupThreads(7)
//  def arRead() = {
//    ar.get()
//  }
//
//  @Benchmark
//  //  @Group("volatile")
//  //  @GroupThreads(1)
//  def arWrite(counter: Counter) = {
//    ar.set(counter)
//  }
//
//  @Benchmark
//  def arUpdate(counter: Counter) = {
//    ar.getAndSet(counter)
//  }

}



@State(Scope.Thread)
class Counter {
  private var x = ThreadLocalRandom.current().nextInt()
  @inline final def step() = {x += 1; x}
}