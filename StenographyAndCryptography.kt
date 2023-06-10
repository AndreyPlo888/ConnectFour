import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Color

// This program helps to hide and read an encrypted message in image

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        var command = readln()
        when (command) {
            "hide" -> hide()
            "show" -> show()
            "exit" -> break
            else -> println("Wrong task: $command")
        }
    }
    println("Bye!")
}

fun hide() {
    println("Input image file:")
    val inputImage = readln()
    println("Output image file:")
    val outputImage = readln()
    println("Message to hide:")
    val message = readln()
    println("Password:")
    val password = readln()
    val messageLine = encode(message, password) + "000000000000000000000011"
    var position = 0

    try {
        val inputFile = File(inputImage)
        val outputFile = File(outputImage)
        val image: BufferedImage = ImageIO.read(inputFile)
        val newImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

        if (messageLine.length + 1 <= image.width * image.height) {
            for (y in 0 until image.height) {
                for (x in 0 until image.width) {
                    if (position == messageLine.length) {
                        val color = Color(image.getRGB(x, y))
                        newImage.setRGB(x, y, color.rgb)
                    } else {
                        val color = Color(image.getRGB(x, y))
                        val red = color.red
                        val green = color.green
                        val blue = (Integer.toBinaryString(color.blue).padStart(8,
                            '0').substring(0,7) + messageLine[position++]).toInt(2)
                        val colorNew = Color(red, green, blue)
                        newImage.setRGB(x, y, colorNew.rgb)
                    }
                }
            }
            ImageIO.write(newImage, "png", outputFile)
            println("Message saved in $outputImage image.")
            return
        } else {
            println("The input image is not large enough to hold this message.")
            return
        }
    }
    catch (e: javax.imageio.IIOException) {
        println("Can't read input file!")
    }
}

fun show() {
    println("Input image file:")
    val inputImage = readln()
    println("Password:")
    val password = readln()
    var message = ""
    var bin = ""
    try {
        val file = File(inputImage)
        val image: BufferedImage = ImageIO.read(file)
        loop@for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val color = Color(image.getRGB(x, y))
                if (bin.contains("000000000000000000000011")) {
                    break@loop
                } else {
                    bin += Integer.toBinaryString(color.blue).last()
                }
            }
        }
        bin = bin.substring(0, bin.length - 24)
        message = decode(bin, password)
        println("Message:")
        println(message)
        return
    }
    catch (e: javax.imageio.IIOException) {
        println("Can't read input file!")
    }
}

fun encode(message: String, password: String): String{
    var resalt = ""
    for (i in 0 until message.length) {
        if (i >= password.length) {
            resalt += Integer.toBinaryString((message[i].toInt() xor password[i
                    % password.length].toInt())).padStart(8, '0')
        } else {
            resalt += Integer.toBinaryString((message[i].toInt() xor password[i].toInt()))
                .padStart(8, '0')
        }
    }
    return resalt
}

fun decode(code: String, password: String): String{
    var resalt = ""
    val list: MutableList<Int> = mutableListOf()
    for (i in 0 until code.length step 8) {
        list.add((code.substring(i, i + 8)).toInt(2))
    }
    for (i in 0 until list.size) {
        if (i >= password.length) {
            resalt += (list[i] xor password[i % password.length].toInt()).toChar()
        } else {
            resalt += (list[i] xor password[i].toInt()).toChar()
        }
    }
    return resalt
}


// C:\Users\anplo\Downloads\24bitttt.png
