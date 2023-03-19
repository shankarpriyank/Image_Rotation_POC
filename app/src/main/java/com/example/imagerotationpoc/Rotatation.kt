package com.example.imagerotationpoc


import android.mediautil.image.jpeg.LLJTran
import android.mediautil.image.jpeg.LLJTranException
import android.os.Environment
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class Rotation() {

    fun rotateImage(imageFile: File) {


        Log.d("Trying to rotate image with LLJTran", "Starting")

        val path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

        val tt = System.currentTimeMillis()
        val file: File = File(path, "$tt.jpg")

        val output = file

        val rotated = try {
            val lljTran = LLJTran(imageFile)
            lljTran.read(
                LLJTran.READ_ALL,
                false
            ) // This could throw an LLJTranException. I am not catching it for now... Let's see.
            lljTran.transform(
                LLJTran.ROT_180,
                LLJTran.OPT_DEFAULTS or LLJTran.OPT_XFORM_ORIENTATION
            )
            BufferedOutputStream(FileOutputStream(output)).use { writer ->
                lljTran.save(writer, LLJTran.OPT_WRITE_ALL)
            }
            lljTran.freeMemory()
            true
        } catch (e: LLJTranException) {
            Log.e(
                e.toString(),
                "Error occurred while trying to rotate image with LLJTrans (AndroidMediaUtil)."
            )
            false
        }

        if (rotated) {
            Log.d("Done rotating image", "rr")
            Log.d("Add", output.absolutePath)
        }
    }
}