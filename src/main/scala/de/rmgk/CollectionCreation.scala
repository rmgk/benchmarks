package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.annotation.tailrec
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.immutable.{
  HashMap, HashSet, IndexedSeq, LinearSeq, List, Map, Queue, Set, Stream, TreeMap, TreeSet, Vector
}
import scala.collection.mutable

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class CollectionCreation {

  @Param(Array( /*"1", "10", "100", "1000",*/ "10000"))
  var size: Int = _

  //immutable

  @tailrec
  final def makeImmutableSet(n: Int, acc: Set[Int]): Set[Int] =
    if (n > 0) makeImmutableSet(n - 1, acc + n) else acc

  @tailrec
  final def makeImmutableMap(n: Int, acc: Map[Int, Int]): Map[Int, Int] =
    if (n > 0) makeImmutableMap(n - 1, acc.+((n, n))) else acc

  @tailrec
  final def makeImmutableIndexedSeq(n: Int, acc: IndexedSeq[Int]): IndexedSeq[Int] =
    if (n > 0) makeImmutableIndexedSeq(n - 1, acc :+ n) else acc

  @tailrec
  final def makeImmutableLinearSeq(n: Int, acc: LinearSeq[Int]): LinearSeq[Int] =
    if (n > 0) makeImmutableLinearSeq(n - 1, n +: acc) else acc

  @tailrec
  final def makeImmutableList(n: Int, acc: List[Int]): List[Int] =
    if (n > 0) makeImmutableList(n - 1, n :: acc) else acc

  //mutable

  @tailrec
  final def makeMutableArray(n: Int, acc: Array[Integer]): Array[Integer] =
    if (n > 0) { acc(acc.length - n) = n; makeMutableArray(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableMap(n: Int, acc: mutable.Map[Int, Int]): mutable.Map[Int, Int] =
    if (n > 0) { acc.+=((n, n)); makeMutableMap(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableSet(n: Int, acc: mutable.Set[Int]): mutable.Set[Int] =
    if (n > 0) { acc += n; makeMutableSet(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableSeq(n: Int, acc: mutable.Seq[Int]): mutable.Seq[Int] =
    if (n > 0) { acc :+ n; makeMutableSeq(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableArrayBuffer(n: Int, acc: mutable.ArrayBuffer[Int]): mutable.Seq[Int] =
    if (n > 0) { acc += n; makeMutableArrayBuffer(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableLinearSeq(n: Int, acc: mutable.Seq[Int]): mutable.Seq[Int] =
    if (n > 0) { n +: acc; makeMutableLinearSeq(n - 1, acc) }
    else acc

  @tailrec
  final def makeMutableBuffer(n: Int, acc: mutable.Buffer[Int]): mutable.Buffer[Int] =
    if (n > 0) { acc += n; makeMutableBuffer(n - 1, acc) }
    else acc

  //java

  @tailrec
  final def makeJavaCollection(n: Int, acc: java.util.Collection[Int]): java.util.Collection[Int] =
    if (n > 0) { acc.add(n); makeJavaCollection(n - 1, acc) }
    else acc

  // ********************************************
  // benchmark methods
  // ********************************************

  //immutable

  @Benchmark
  def immutableSetHash() = makeImmutableSet(size, HashSet[Int]())

  @Benchmark
  def immutableSetTree() = makeImmutableSet(size, TreeSet[Int]())

  //  @Benchmark
  //  def immutableSetList() = makeImmutableSet(size, ListSet[Int]())

  @Benchmark
  def immutableMapHash() = makeImmutableMap(size, HashMap[Int, Int]())

  @Benchmark
  def immutableMapTree() = makeImmutableMap(size, TreeMap[Int, Int]())

  //  @Benchmark
  //  def immutableMapList() = makeImmutableMap(size, ListMap[Int, Int]())

  @Benchmark
  def immutableIndexedSeqVector() = makeImmutableIndexedSeq(size, Vector[Int]())

  @Benchmark
  def immutableLinearSeqList() = makeImmutableLinearSeq(size, List[Int]())

  @Benchmark
  def immutableLinearSeqStream() = makeImmutableLinearSeq(size, Stream[Int]())

  @Benchmark
  def immutableLinearSeqQueue() = makeImmutableLinearSeq(size, Queue[Int]())

  @Benchmark
  def immutableList() = makeImmutableList(size, List())

  @Benchmark
  def immutableListFill() = {
    var i = 0
    List.fill(size) { i += 1; i }
  }

  //mutable

  @Benchmark
  def mutableArray() = makeMutableArray(size, new Array[Integer](size))

  @Benchmark
  def mutableMapHash() = makeMutableMap(size, mutable.HashMap[Int, Int]())

  @Benchmark
  def mutableMapLinkedHash() = makeMutableMap(size, mutable.LinkedHashMap[Int, Int]())

  //  @Benchmark
  //  def mutableMapList() = makeMutableMap(size, mutable.ListMap[Int, Int]())

  @Benchmark
  def mutableMapOpenHash() = makeMutableMap(size, mutable.OpenHashMap[Int, Int]())

  @Benchmark
  def mutableSetHash() = makeMutableSet(size, mutable.HashSet[Int]())

  @Benchmark
  def mutableSetLinkedHash() = makeMutableSet(size, mutable.LinkedHashSet[Int]())

  @Benchmark
  def mutableSeqArray() = makeMutableSeq(size, mutable.ArraySeq[Int]())

  @Benchmark
  def mutableSeqArrayBuffer() = makeMutableBuffer(size, mutable.ArrayBuffer[Int]())

  @Benchmark
  def mutableSeqListBuffer() = makeMutableBuffer(size, mutable.ListBuffer[Int]())

  @Benchmark
  def mutableSeqStack() = makeMutableSeq(size, mutable.Stack[Int]())

  @Benchmark
  def mutableSeqArrayStack() = makeMutableSeq(size, mutable.ArrayStack[Int]())

  // java

  @Benchmark
  def javaArrayList() = makeJavaCollection(size, new java.util.ArrayList[Int]())
  @Benchmark
  def javaArrayListSizeHint() = makeJavaCollection(size, new java.util.ArrayList[Int](size))
  @Benchmark
  def javaVector() = makeJavaCollection(size, new java.util.Vector[Int]())
  @Benchmark
  def javaVectorSizeHint() = makeJavaCollection(size, new java.util.Vector[Int](size))
  @Benchmark
  def javaLinkedList() = makeJavaCollection(size, new java.util.LinkedList[Int]())
  @Benchmark
  def javaHashSet() = makeJavaCollection(size, new java.util.HashSet[Int]())
  @Benchmark
  def javaAsScalaArrayList() = makeMutableBuffer(size, new java.util.ArrayList[Int]().asScala)
  @Benchmark
  def javaAsScalaArrayListSizeHint() = makeMutableBuffer(size, new java.util.ArrayList[Int](size).asScala)
}
