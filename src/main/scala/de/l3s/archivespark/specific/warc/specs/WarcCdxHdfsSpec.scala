/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2018 Helge Holzmann (L3S) and Vinay Goel (Internet Archive)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.l3s.archivespark.specific.warc.specs

import de.l3s.archivespark.dataspecs.TextDataLoader
import de.l3s.archivespark.specific.warc.{CdxRecord, WarcRecord}

import scala.util.Try

class WarcCdxHdfsSpec private(cdxPath: String, warcPath: String) extends WarcHdfsSpecBase[String] with TextDataLoader {
  override def dataPath: String = cdxPath

  val warcPathMap = filePathMap(warcPath)

  override def parse(data: String): Option[WarcRecord] = {
    CdxRecord.fromString(data).flatMap{cdx =>
      Try{cdx.additionalFields(1)}.toOption.flatMap(warcPathMap.pathToFile).flatMap(parse(cdx, _))
    }
  }
}

object WarcCdxHdfsSpec {
  def apply(cdxPath: String, warcPath: String) = new WarcCdxHdfsSpec(cdxPath, warcPath)
  def apply(path: String) = new WarcCdxHdfsSpec(path + "/*.cdx", path)
}