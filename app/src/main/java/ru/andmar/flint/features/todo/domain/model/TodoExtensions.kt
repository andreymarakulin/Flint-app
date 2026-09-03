package ru.andmar.flint.features.todo.domain.model

fun isTodoAction(todoDetails: TodoDetails): Boolean {
    return todoDetails.title.isNotBlank() || todoDetails.text.isNotBlank()
}