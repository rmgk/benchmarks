package de.rmgk

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Group)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@Threads(16)
class ConcurrentMapRead {

	var concurrentMap: java.util.concurrent.ConcurrentMap[String, String] = _
	var javaMap: java.util.Map[String, String] = _
	var immutableScalaMap: scala.collection.immutable.Map[String, String] = _
	var mutableScalaMap: scala.collection.mutable.Map[String, String] = _

	def values = List("one", "tow", "three", "four", "five", "six", "seven")

	@Setup(Level.Iteration)
	def setup() = {
		concurrentMap = new java.util.concurrent.ConcurrentHashMap[String, String]()
		javaMap = new java.util.HashMap[String, String]()
		immutableScalaMap = scala.collection.immutable.TreeMap[String, String](values.zip(values): _*)
		mutableScalaMap = scala.collection.mutable.HashMap[String, String](values.zip(values): _*)
		values.foreach { v =>
			concurrentMap.put(v, v)
			javaMap.put(v, v)
			//scalaMap.+=(Tuple2(v, v))
		}

	}

	@Benchmark
	@Group("concurrentMap")
	def readConcurrent(bh: Blackhole) = {
		bh.consume(concurrentMap.get("six"))
	}

	@Benchmark
	@Group("javaMap")
	def readNormal(bh: Blackhole) = {
		bh.consume(javaMap.get("six"))
	}

	@Benchmark
	@Group("scalaMapImmutable")
	def readScalaImmutable(bh: Blackhole) = {
		bh.consume(immutableScalaMap.apply("six"))
	}

	@Benchmark
	@Group("scalaMapMutable")
	def readScalaMutable(bh: Blackhole) = {
		bh.consume(mutableScalaMap.apply("six"))
	}


}
