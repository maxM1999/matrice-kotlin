package com.example.matrices

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import java.lang.Float.max
import kotlin.random.Random

class MyButton(context: Context) : AppCompatButton(context){
    private val particleRadius = 50f
    private val particlePaint = Paint().apply { color = Color.RED }
    private val particles = mutableListOf<Particle>()
    private var clickX = 0f
    private var clickY = 0f


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            clickX = event.x
            clickY = event.y
        }

        return super.onTouchEvent(event)
    }

    // Quand le bouton est cliqué
    override fun performClick(): Boolean {
        generateParticles()
        invalidate()

        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for(particle in particles){
            // Enregistre le transform du canavas du bouton
            canvas.save()

            // déplace le canvas a la pos de la particle
            canvas.translate(particle.x, particle.y)

            // rotationne le canvas selon la rot de la particle
            canvas.rotate(particle.rotation)

            // modifie le scale de chaque particle
            canvas.scale(1.5f, 1.5f)

            // dessine un rectangle qui représente la particle
            canvas.drawRect(0f, 0f, particle.radius, particle.radius, particlePaint)

            // réassigne le transforme sauvegardé plus haut au canvas
            canvas.restore()
        }

        //updateParticles()
        updateParticles()

        // remove if not visible
        particles.removeAll {!it.isVisible()}

        if(particles.isNotEmpty()){
            postInvalidateOnAnimation()
        }
    }

    private fun generateParticles(){
        for(i in 0 .. 20){
            particles.add(Particle(clickX, clickY, particleRadius))
        }
    }

    private fun updateParticles(){
        for(particle in particles){
            particle.x += particle.vx
            particle.y += particle.vy
            particle.rotation += 1.0f
            particle.vx += Random.nextFloat() * 2f - 1f
            particle.vy += Random.nextFloat() * 2f - 1f
            particle.radius = max(0f, particle.radius - 0.5f)
        }
    }

    private data class Particle(var x:Float, var y:Float, var radius:Float, var vx:Float = 0f, var vy:Float = 0f, var rotation:Float = 0f)
    {
        fun isVisible() = radius > 0f
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        addButton = MyButton(this)
        addButton.text = "Animate"
        addButton.setOnClickListener{
            /*val startX = it.scaleX
            val startY = it.scaleY
            it.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .withEndAction{
                    it.animate().scaleX(startX).scaleY(startY).duration = 250
                }.duration = 250*/
        }

        linearLayout.addView(addButton)
        setContentView(linearLayout)


    }
}