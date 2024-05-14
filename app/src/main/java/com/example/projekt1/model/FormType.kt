package com.example.projekt1.model

import java.io.Serializable

sealed class FormType: Serializable {
    data object New: FormType() {
        private fun readResolve(): Any = New
    }

    data class Edit(val id: Int): FormType()
}