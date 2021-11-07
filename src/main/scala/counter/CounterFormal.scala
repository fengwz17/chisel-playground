package counter

class CounterProp(width: Int) extends Counter(width) {}

object CounterFormal extends App {
  (new chisel3.stage.ChiselStage).emitFirrtl(new Counter(2), Array("--target-dir", "build"))
  (new chisel3.stage.ChiselStage).emitFirrtl(new CounterProp(2), Array("--target-dir", "build"))
}
