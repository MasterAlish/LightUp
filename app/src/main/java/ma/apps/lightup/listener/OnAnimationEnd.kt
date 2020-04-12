package ma.apps.lightup.listener

import android.view.animation.Animation

class OnAnimationEnd(val onEnd: ()->Unit):Animation.AnimationListener{
    override fun onAnimationRepeat(p0: Animation?) {

    }

    override fun onAnimationEnd(p0: Animation?) {
        onEnd()
    }

    override fun onAnimationStart(p0: Animation?) {

    }
}