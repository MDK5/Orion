package de.tum.www1.orion.ui

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import de.tum.www1.orion.exercise.registry.OrionInstructorExerciseRegistry
import de.tum.www1.orion.settings.OrionSettingsProvider

class OrionRouterService(private val project: Project): OrionRouter {

    override fun routeForCurrentExercise(): String? {
        val registry = project.service<OrionInstructorExerciseRegistry>()
        return if (registry.isArtemisExercise) {
            registry.exerciseInfo?.let {
                return if (registry.isOpenedAsInstructor) {
                    "${defaultRoute()}$CODE_EDITOR_INSTRUCTOR_URL".format(it.courseId, it.exerciseId, it.templateParticipationId)
                } else {
                    "${defaultRoute()}$EXERCISE_DETAIL_URL".format(it.courseId, it.exerciseId)
                }
            }
        } else {
            null
        }
    }

    override fun defaultRoute(): String =
            ServiceManager.getService(OrionSettingsProvider::class.java).getSetting(OrionSettingsProvider.KEYS.ARTEMIS_URL)

    companion object {
        private const val EXERCISE_DETAIL_URL = "/#/courses/%d/exercises/%d"
        private const val CODE_EDITOR_INSTRUCTOR_URL = "/#/course-management/%d/programming-exercises/%d/code-editor/ide/%d"
    }
}
