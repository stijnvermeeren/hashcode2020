package challenge

final case class Config(
  exponent: Double
)

object Config {
  val default: Config = Config(exponent = 1.0)
}
