package com.example.mvvmlab

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {
    // Private mutable state: only the ViewModel can change it
    private val _tasks = MutableStateFlow(repository.loadInitialTasks())
    // Public read-only state: the UI can observe it but not modify it
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
    // Events from the UI come in as functions
    fun toggleDone(taskId: Int) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) task.copy(done = !task.done) else task
            }
        }
    }
    fun addTask(title: String) {
        if (title.isBlank()) return
        _tasks.update { list ->
            val nextId = (list.maxOfOrNull { it.id } ?: 0) + 1
            list + Task(id = nextId, title = title.trim())
        }
    }
}