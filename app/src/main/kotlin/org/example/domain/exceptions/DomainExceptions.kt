package org.example.domain.exceptions

class EntityNotFoundException(message: String) : RuntimeException(message)

class ProfileAccessDeniedException(message: String) : RuntimeException(message)
