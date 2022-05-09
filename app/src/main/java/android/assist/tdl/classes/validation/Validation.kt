package android.assist.tdl.classes.validation

// Validation class with the functions for validating

class Validation {

    companion object {

        fun textValidation(text : String) : Boolean {
            return text.isNotEmpty()
        }

        fun dateValidation(text: String) : Boolean {
            if ( text.length != 10 ) {
                return false
            } else if ( text.isEmpty() ) {
                return false
            }
            return true
        }
    }
}