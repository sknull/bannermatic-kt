package de.visualdigits.kotlin.bannermatic.main

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@ExperimentalCli
class BannerMatic {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val parser = ArgParser("bannermatic-kt")
            parser.subcommands(ImageCommand(), TextCommand(), BannerCommand())
            parser.parse(args)
        }
    }
}