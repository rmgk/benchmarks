
object Testcase {

  def main(args: Array[String]): Unit = {

    val outer = 4
    trait T {
      def a = outer
    }
    object O extends T {
      def b = outer
    }

    println(O)
  }

}
