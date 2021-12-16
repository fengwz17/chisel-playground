package regfile

import chisel3._

class WriteBack extends Bundle {
  val valid = Input(Bool())
  val dest  = Input(UInt(8.W))
  val data  = Input(UInt(64.W))
}

class State extends Bundle {
  val reg = Output(Vec(32, UInt(64.W)))
}

class RegFile extends Module {
  val io = IO(new Bundle {
    val wb    = Input(new WriteBack)
    val state = Output(new State)
    val rd = new Bundle {
      val idx  = Input(UInt(8.W))
      val data = Output(UInt(64.W))
    }
  })

  val reg = RegInit(VecInit(Seq.fill(32)(0.U(64.W))))

  when(io.wb.valid) {
    reg(io.wb.dest) := io.wb.data
  }

  io.state.reg := reg

  io.rd.data := reg(io.rd.idx)
}

class RegFileChecker extends Module {
  val io = IO(new Bundle {
    val wb    = Input(new WriteBack)
    val state = Input(new State)
    val rd = new Bundle {
      val idx  = Input(UInt(8.W))
      val data = Input(UInt(64.W))
    }
  })

  val regFile = Module(new RegFile)

  regFile.io.wb     := io.wb
  regFile.io.rd.idx := io.rd.idx

  for (i <- 0 until 32) {
    assert(regFile.io.state.reg(i.U) === io.state.reg(i.U))
  }
  assert(regFile.io.rd.data === io.rd.data)
}
