package ru.andmar.flint.features.note.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class DetailsUseCase(
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository
) {

    fun getNoteById(noteId: String): Flow<NoteDetails> {
        return noteRepository.getNoteById(noteId)
    }

    fun getTodos(): Flow<List<TodoDetails>> {
        return todoRepository.getTodos()
    }
}