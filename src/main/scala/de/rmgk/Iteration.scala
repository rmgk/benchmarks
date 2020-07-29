package de.rmgk

import java.util.Collections
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import scala.collection.mutable.ArrayBuffer

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class Iteration {

  @Param(Array( /*"1", "10", "100", "1000",*/ "100000000"))
  var size: Int = _

  val list        = List.fill(size)(1)
  val arrayList   = new java.util.ArrayList[Int](Collections.nCopies(size, 0))
  val arrayBuffer = ArrayBuffer.fill(size)(1)

  @Benchmark
  def foreachList(bh: Blackhole) = list.foreach(bh.consume)

  @Benchmark
  def iterateArrayList(bh: Blackhole) = {
    val it = arrayList.iterator()
    while (it.hasNext) bh.consume(it.next())
  }

  @Benchmark
  def foreachArrayList(bh: Blackhole) = arrayList.forEach(i => bh.consume(i))

  @Benchmark
  def foreachArrayBuffer(bh: Blackhole) = arrayBuffer.foreach(bh.consume)

  @Benchmark
  def iteratorArrayBuffer(bh: Blackhole) = arrayBuffer.iterator.foreach(bh.consume)

  @Benchmark
  def iterateArrayBuffer(bh: Blackhole) = {
    println(arrayBuffer.size)
    val it = arrayBuffer.iterator
    while (it.hasNext) bh.consume(it.next())
  }

}
