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

    private val _showOnlyOpen = MutableStateFlow(false)
    val showOnlyOpen: StateFlow<Boolean> = _showOnlyOpen.asStateFlow()


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

    fun deleteTask(taskId: Int) {
        _tasks.update { list ->
            list.filter { task -> task.id != taskId }
        }
    }

    fun setShowOnlyOpen(onlyOpen: Boolean) {
        _showOnlyOpen.value = onlyOpen
    }

    fun getVisibleTasks(tasks: List<Task>, onlyOpen: Boolean): List<Task> {
        return if (onlyOpen) {
            tasks.filter { !it.done }
        } else {
            tasks
        }
    }

}