#!/bin/bash
exec scala -savecompiled "$0" "$@"
!#

import scala.xml.Elem
class Triangle(val len: Double, val x1: Double, val y1: Double) {
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

object SVG {
  val sqrt3 = 1.7320508075688772
  def getSvg(objects: List[Triangle]):Elem = {
    val xml = 
      <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
        { objects map {_.getXML} }
      </svg>
    xml
  }

  def bigLeft(triangle: Triangle):List[Triangle] = {
    List(
      new Triangle(triangle.len/2,
        triangle.x1 - triangle.len * .25,
        triangle.y1 + (triangle.len/2*sqrt3/2)
      )
    )
  }

  def triforce(triangle: Triangle):List[Triangle] = {
    List(new Triangle(triangle.len/4, triangle.x1, triangle.y1),
      new Triangle(triangle.len/4,
        triangle.x1 - (triangle.len/8),
        triangle.y1 + (triangle.len/4*sqrt3/2)),
      new Triangle(triangle.len/4,
        triangle.x1 + (triangle.len/8),
        triangle.y1 + (triangle.len/4*sqrt3/2))
    )
  }

  def iterate(triangle: Triangle, dims: List[Triangle], iters: Int):List[Triangle] = {
    if (iters == 0) dims ++ List(triangle)
    else  {
      val smallTriangle = 
        new Triangle(triangle.len/2, 
          triangle.x1 + triangle.len * .25,
          triangle.y1 + (triangle.len/2*sqrt3/2)
        )
      dims ++ bigLeft(triangle) ++ triforce(triangle) ++ iterate(smallTriangle, dims, iters -1)
    }
  }

  def output(len: Double = 1000.0, iterations: Int = 256):Unit = {
    val tris = iterate(new Triangle(len, len / 2, 0.0), List(), iterations)
    println(getSvg(tris))
  }
}

val argList = argv.toList.map(_.toInt)
if (argList.length >= 2)
  SVG.output(argList(0), argList(1))
else
  SVG.output()
