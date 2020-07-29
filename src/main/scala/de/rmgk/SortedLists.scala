package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class SortedLists {
  @Param(Array("100000"))
  var size: Int = _

  lazy val list       = List.tabulate(size)(_ => Random.nextString(10))
  lazy val listSorted = list.sorted

  lazy val array       = list.toArray
  lazy val arraySorted = list.toArray

  lazy val listSortedNew = arraySorted.toList

  @Benchmark
  def iterateUnsorted(bh: Blackhole): Unit = {
    list.view.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def iterateSorted(bh: Blackhole): Unit = {
    listSorted.view.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def iterateSortedNew(bh: Blackhole): Unit = {
    listSortedNew.view.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def iterateArray(bh: Blackhole): Unit = {
    array.view.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def iterateArraySorted(bh: Blackhole): Unit = {
    arraySorted.view.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def mapUnsorted(bh: Blackhole): Unit = {
    list.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def mapSorted(bh: Blackhole): Unit = {
    listSorted.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def mapArray(bh: Blackhole): Unit = {
    array.map(_.hashCode()).foreach(bh.consume)
  }

  @Benchmark
  def mapArraySorted(bh: Blackhole): Unit = {
    arraySorted.map(_.hashCode()).foreach(bh.consume)
  }
}
