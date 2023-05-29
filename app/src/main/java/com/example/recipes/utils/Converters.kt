package com.example.recipes.utils

import com.example.recipes.domain.models.Ingredient

fun List<Ingredient>.convertIngredients(): String {
        var convertedText = this.joinToString { element ->
            if (element.unit == "") {
                "- ${element.amount} ${element.name}"
            } else {
                "- ${element.amount} ${element.unit} of ${element.name}"
            }
        }
        convertedText = convertedText.replace(", ", ",\n")
        return convertedText
    }

    fun String.convertInstructions(): String {
        val oldStr = listOf("<ol><li>", "</li></ol>", "</li><li>")
        var convertedText = this
        for (element in oldStr) {
            if (convertedText.contains(element)) {
                convertedText = convertedText.replace(
                    element, ""
                )
            }
        }
        convertedText = convertedText.replace(". ", ".\n")
        return convertedText
    }