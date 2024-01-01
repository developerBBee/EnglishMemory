package jp.developer.bbee.englishmemory.domain.model

data class BookmarkPreferences(
    val searchWord: String,
    val bookmarkBooleans: Map<FilterKey, Boolean>,
    val sortPrefs: Map<SortKey, Order>,
) {

    companion object {
        fun from(
            searchWord: String,
            bookmarkBooleans: Map<FilterKey, Boolean>,
            sortPrefs: Map<SortKey, Order>,
        ): BookmarkPreferences {
            return BookmarkPreferences(
                searchWord = searchWord,
                bookmarkBooleans = bookmarkBooleans,
                sortPrefs = sortPrefs,
            )
        }
    }
}

enum class SearchKey(val displayName: String) {
    SEARCH_WORD("英単語検索"),
}

enum class FilterKey(val displayName: String, val type: FilterKeyType) {
    IMPORTANCE_RANK_A("A", FilterKeyType.IMPORTANCE),
    IMPORTANCE_RANK_B("B", FilterKeyType.IMPORTANCE),
    IMPORTANCE_RANK_C("C", FilterKeyType.IMPORTANCE),
    WORD_TYPE_NOUN("名詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_PRONOUN("代名詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_VERB("動詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_ADJECTIVE("形容詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_ADVERB("副詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_AUXILIARY_VERB("助動詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_PREPOSITION("前置詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_INTERJECTION("間投詞", FilterKeyType.WORD_TYPE),
    WORD_TYPE_CONJUNCTION("接続詞", FilterKeyType.WORD_TYPE),
    ;

    companion object {
        fun fromDisplayName(displayName: String): FilterKey? {
            return entries.find { it.displayName == displayName }
        }
    }
}

enum class FilterKeyType {
    IMPORTANCE,
    WORD_TYPE
}

enum class SortKey(val displayName: String) {
    ENGLISH("単語順"),
    NUMBER_OF_QUESTION("出題回数順"),
    SCORE_RATE("正答率順"),
    COUNT_MISS("不正解数順"),
    COUNT_CORRECT("正解数順"),
}

enum class Order {
    NONE,
    ASC,
    DESC,
}