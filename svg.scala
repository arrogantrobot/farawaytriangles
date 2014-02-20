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
  def getXML = <polygon points={coordinateString} style="fill:rgb(0,0,0);stroke-width:1;stroke:rgb(0,0,0)" />
  override def toString = len +" "+ x1 +" "+ y1
}

object SVG {
  val sqrt3 = 1.7320508075688772
  def getSvg(objects: List[Triangle]):Elem = {
    val xml = <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
      { objects map {_.getXML} }
    </svg>
    xml
  }

  def bigLeft(c: Triangle):List[Triangle] = {
    List(new Triangle(c.len/2, c.x1 - c.len * .25, c.y1 + (c.len/2*sqrt3/2)))
  }

  def triforce(c: Triangle):List[Triangle] = {
    List(new Triangle(c.len/4, c.x1, c.y1),
      new Triangle(c.len/4, c.x1 - (c.len/8), c.y1 + (c.len/4*sqrt3/2)),
      new Triangle(c.len/4, c.x1 + (c.len/8), c.y1 + (c.len/4*sqrt3/2)))
  }

  def iterate(c: Triangle, dims: List[Triangle], iters: Int):List[Triangle] = {
    if (iters == 0) dims
    else 
      val t = new Triangle(c.len/2, c.x1 + c.len * .25, c.y1 + (c.len/2*sqrt3/2))
      dims ++ bigLeft(c) ++ triforce(c) ++ iterate(t, dims, iters -1)
  }

  def output():Unit = {
    val tris = iterate(new Triangle(1000, 1000, 0), List(), 256)
    println(getSvg(tris))
  }
}

SVG.output
