circuit Inverter :
  module Inverter :
    input clock : Clock
    input reset : UInt<1>
    input in : UInt<1>
    output out : UInt<1>
    input hold : UInt<1>

    reg delay : UInt<1>, clock with :
      reset => (UInt<1>("h0"), delay) @[Inverter.scala 15:17]
    node _T = eq(hold, UInt<1>("h0")) @[Inverter.scala 16:7]
    when _T : @[Inverter.scala 16:14]
      node _delay_T = eq(in, UInt<1>("h0")) @[Inverter.scala 17:13]
      delay <= _delay_T @[Inverter.scala 17:10]
    out <= delay @[Inverter.scala 19:6]

