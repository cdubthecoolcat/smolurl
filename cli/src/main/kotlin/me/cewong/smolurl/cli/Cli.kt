package me.cewong.smolurl.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
  val parser = ArgParser("smolurl")
  val target by parser.option(
    ArgType.String,
    shortName = "t",
    fullName = "target",
    description = "The target URL to shorten"
  ).required()
  val alias by parser.option(
    ArgType.String,
    shortName = "a",
    fullName = "alias",
    description = "The custom alias to use"
  )
  parser.parse(args)
  println("Target: $target")
  println("Alias: $alias")
  // TODO Make request and shorten url
}
