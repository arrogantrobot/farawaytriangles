package com.farawaylabs.scala.logo

import java.io.{File, PrintWriter}

import scala.xml.Elem

case class Triangle(val len: Double, val x1: Double, val y1: Double) {
  val sqrt3 = 1.7320508075688772
  val x2 = x1 + len/2 
  val y2 = y1 + sqrt3/2 * len
  val x3 = x1 - len/2
  val y3 = y1 + sqrt3/2 * len
  val coordinateString = x1+","+y1+" "+x2+","+y2+" "+x3+","+y3
  val style = "fill:rgb(0,0,0);stroke-width:0;stroke:rgb(0,0,0)"
  def getXML = <polygon points={coordinateString} style={style} />
  override def toString = len +" "+ x1 +" "+ y1
}

object Triangle {
  implicit val tsf = 2
  def getRightSmallTriangle(triangle:Triangle, tsf:Int = tsf):Triangle = {
    new Triangle(triangle.len/2,
      triangle.x1 - triangle.len * .25,
      triangle.y1 + (triangle.len/2*triangle.sqrt3/2)
    )
  }

  def getRightSmallTriangle4(triangle:Triangle, tsf:Int = tsf):Triangle = {
    getRightSmallTriangle(triangle, 4)
  }

  def getLeftSmallTriangle(triangle:Triangle, tsf:Int = tsf):Triangle = {
    new Triangle(triangle.len/2,
      triangle.x1 + triangle.len * .25,
      triangle.y1 + (triangle.len/2*triangle.sqrt3/2)
    )
  }

  def getLeftSmallTriangle4(triangle:Triangle, tsf:Int = tsf):Triangle = {
    getLeftSmallTriangle(triangle, 4)
  }

  def getSmallTriangle(triangle:Triangle, tsf:Int = tsf):Triangle = {
    new Triangle(triangle.len/2,
      triangle.x1,
      triangle.y1 //+ (triangle.len/2*triangle.sqrt3/2)
    )
  }

  def getSmallTriangle4(triangle:Triangle, tsf:Int = tsf):Triangle = {
    getSmallTriangle(triangle, 4)
  }

  def triforce(triangle: Triangle, tsf:Int = tsf):List[Triangle] = {
    List(new Triangle(triangle.len/tsf, triangle.x1, triangle.y1),
      new Triangle(triangle.len/tsf,
        triangle.x1 - (triangle.len/(tsf*2)),
        triangle.y1 + (triangle.len/tsf*triangle.sqrt3/2)),
      new Triangle(triangle.len/tsf,
        triangle.x1 + (triangle.len/(tsf*2)),
        triangle.y1 + (triangle.len/tsf*triangle.sqrt3/2))
    )
  }

  def triforce4(triangle: Triangle, tsf:Int = tsf):List[Triangle] = {
    triforce(triangle, 4)
  }
}

object SVG {
  val sqrt3 = 1.7320508075688772
  def getSvg(objects: List[Triangle]):Elem = {
    println(s"drawing ${objects.length} triangles")
    val xml =
      <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
        { objects.map(_.getXML) }
      </svg>
    xml
  }

  def iterateTri(triangle: Triangle, dims: List[Triangle], iters: Int):List[Triangle] = {
    if (iters == 0) dims ++ Triangle.triforce(triangle)
    else  {
      dims ++ iterateBi(Triangle.triforce(triangle)(0), dims, iters -1) ++
       iterateBi(Triangle.triforce(triangle)(1), dims, iters -1) ++
       iterateBi(Triangle.triforce(triangle)(2), dims, iters -1)
    }
  }

  def iterateBi(triangle: Triangle, dims: List[Triangle], iters: Int):List[Triangle] = {
    if (iters == 0) dims ++ Triangle.triforce4(triangle)
    else  {
      dims ++
        Triangle.triforce4(triangle) ++
        iterateTri(Triangle.getRightSmallTriangle4(triangle), dims, iters - 1) ++
        iterateTri(Triangle.getLeftSmallTriangle4(triangle), dims, iters - 1)
      /*
        iterate(Triangle.getRightSmallTriangle(triangle), dims, iters -1)*/
    }
  }

  def output(len: Double = 1000.0, iterations: Int = 12):Unit = {
    val tris = iterateTri(new Triangle(len, len /2, 0.0), List(), iterations)
    val writer = new PrintWriter(new File("triforce.svg"))
    writer.write(getSvg(tris).toString())
    writer.close()

    println(getSvg(tris))
  }

  def main(args: Array[String]) {
    if (args.length >= 2)
      SVG.output(args(0).toInt, args(1).toInt)
    else
      SVG.output()
  }
}
