#!/bin/bash
exec scala -savecompiled "$0" "$@"
!#

class Triangle(val len: Double, val x1: Double, val y1: Double) {
  val sqrt3 = 1.7320508075688772
  val x2 = x1 + len/2 
  val y2 = y1 + sqrt3/2 * len
  val x3 = x1 - len/2
  val y3 = y1 + sqrt3/2 * len
  val coordinateString = x1+","+y1+" "+x2+","+y2+" "+x3+","+y3
  def getXML = <polygon points={coordinateString} style="fill:rgb(0,0,0);stroke-width:1;stroke:rgb(0,0,0)" />
  def unapply(c: coords) = try {
    Some(new Triangle(c.len, c.x1, c.y1))
  } catch {
    case ex: Exception => None
  }
}


class coords(val len: Double, val x1:Double, val y1:Double) {
  override def toString = len +" "+ x1 +" "+ y1
}

object SVG {
  val sqrt3 = 1.7320508075688772
  val tr = new Triangle(100, 100,0)
  def getSvg = 
    <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
      {tr.getXML}
    </svg>

  /*def triTriangles(tri: Triangle):List[Triangle] = {
    List(Triangle(1,1,1))
  }*/

  def bigLeft(c: coords):List[coords] = {
    List(new coords(c.len/2, c.x1 - c.x1 * .5, c.y1 + (c.y1*sqrt3/2)))
  }

  def triforce(c: coords):List[coords] = {
    List(new coords(c.len/4, c.x1, c.y1),
      new coords(c.len/4, c.x1 - c.x1 * .25, c.y1 + (c.y1*sqrt3/4)),
      new coords(c.len/4, c.x1 + c.x1 * .25, c.y1 + (c.y1*sqrt3/4)))
  }

  def makeMagics(c: coords, dims: List[coords], iters: Int):List[coords] = {
    if (iters == 0) dims
    else 
      dims ++ bigLeft(c) ++ triforce(c) ++ makeMagics(c, dims, iters -1)
  }

  def output():Unit = {
    val thing = new coords(100, 100, 0)
    println(getSvg)
    makeMagics(new coords(1000, 1000, 0), List(), 1) map {
      println(_)
    }
  }
}

SVG.output
