package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Measurement, Mode, OutputTimeUnit, Scope, State, Warmup}

import scala.util.{Failure, Try}
import scala.util.control.ControlThrowable

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class Exceptions {
  @Benchmark
  def newThrowable(): Throwable = {
    new Throwable()
  }

  class MyControlThrowable() extends ControlThrowable

  @Benchmark
  def newControlThrowable(): Throwable = {
    new MyControlThrowable()
  }

  class NoStackTrace extends Throwable("", null, false, false)

  @Benchmark
  def newNoStackException(): Throwable = {
    new NoStackTrace()
  }

  @Benchmark
  def catchingExceptions(): Throwable = {
    def fails() = throw new MyControlThrowable()

    try fails() catch {case e: MyControlThrowable => e}
  }

  @Benchmark
  def monadicExceptions(): Try[Int] = {
    def fails() = Failure.apply(new MyControlThrowable)

    fails()
  }
}
