package com.example.tutuproject.others

import com.example.tutuproject.data.models.Character

sealed class RvItemDesigner {
    class CasualItem(val character: Character): RvItemDesigner()
    object Loader : RvItemDesigner()
}