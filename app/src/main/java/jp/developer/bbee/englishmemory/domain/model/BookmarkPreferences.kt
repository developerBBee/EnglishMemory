package jp.developer.bbee.englishmemory.domain.model

data class BookmarkPreferences(
    val searchWord: String,
    val bookmarkBooleans: Map<BookmarkKey, Boolean>,
) {

    companion object {
        private val INITIAL_BOOLEANS = mapOf(
            BookmarkKey.IMPORTANCE_RANK_A to true,
            BookmarkKey.IMPORTANCE_RANK_B to true,
            BookmarkKey.IMPORTANCE_RANK_C to true,
            BookmarkKey.WORD_TYPE_NOUN to true,
            BookmarkKey.WORD_TYPE_PRONOUN to true,
            BookmarkKey.WORD_TYPE_VERB to true,
            BookmarkKey.WORD_TYPE_ADJECTIVE to true,
            BookmarkKey.WORD_TYPE_ADVERB to true,
            BookmarkKey.WORD_TYPE_AUXILIARY_VERB to true,
            BookmarkKey.WORD_TYPE_PREPOSITION to true,
            BookmarkKey.WORD_TYPE_INTERJECTION to true,
            BookmarkKey.WORD_TYPE_CONJUNCTION to true,
        )

        fun from(
            searchWord: String = "",
            bookmarkBooleans: Map<BookmarkKey, Boolean> = INITIAL_BOOLEANS,
        ): BookmarkPreferences {
            return BookmarkPreferences(
                searchWord = searchWord,
                bookmarkBooleans = bookmarkBooleans,
            )
        }
    }
}

enum class BookmarkKey(val displayName: String, val type: BookmarkType) {
    SEARCH_WORD("英単語検索", BookmarkType.SEARCH),
    IMPORTANCE_RANK_A("A", BookmarkType.IMPORTANCE),
    IMPORTANCE_RANK_B("B", BookmarkType.IMPORTANCE),
    IMPORTANCE_RANK_C("C", BookmarkType.IMPORTANCE),
    WORD_TYPE_NOUN("名詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_PRONOUN("代名詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_VERB("動詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_ADJECTIVE("形容詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_ADVERB("副詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_AUXILIARY_VERB("助動詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_PREPOSITION("前置詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_INTERJECTION("間投詞", BookmarkType.WORD_TYPE),
    WORD_TYPE_CONJUNCTION("接続詞", BookmarkType.WORD_TYPE),
}

enum class BookmarkType {
    SEARCH,
    IMPORTANCE,
    WORD_TYPE
}