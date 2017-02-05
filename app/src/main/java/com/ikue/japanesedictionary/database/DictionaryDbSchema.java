package com.ikue.japanesedictionary.database;

/**
 * Created by luke_c on 03/02/2017.
 */

// TODO: Add Jmnedict data
public class DictionaryDbSchema {

    public static final class Jmdict {

        public static final class KanjiElementTable {
            public static final String NAME = "Jmdict_Kanji_Element";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class ReadingElementTable {
            public static final String NAME = "Jmdict_Reading_Element";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
                public static final String VALUE = "VALUE";
                public static final String IS_TRUE_READING = "NO_KANJI";
            }
        }

        public static final class ReadingRelationTable {
            public static final String NAME = "Jmdict_Reading_Relation";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
                public static final String READING_ELEMENT_ID = "READING_ELEMENT_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class SenseElementTable {
            public static final String NAME = "Jmdict_Sense_Element";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
            }
        }

        public static final class SensePosTable {
            public static final String NAME = "Jmdict_Sense_Pos";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String SENSE_ID = "SENSE_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class SenseFieldTable {
            public static final String NAME = "Jmdict_Sense_Field";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String SENSE_ID = "SENSE_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class SenseDialectTable {
            public static final String NAME = "Jmdict_Sense_Dialect";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String SENSE_ID = "SENSE_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class GlossTable {
            public static final String NAME = "Jmdict_Gloss";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
                public static final String SENSE_ID = "SENSE_ID";
                public static final String VALUE = "VALUE";
            }
        }

        public static final class PriorityTable {
            public static final String NAME = "Jmdict_Priority";

            public static final class Cols {
                public static final String _ID = "_ID";
                public static final String ENTRY_ID = "ENTRY_ID";
                public static final String VALUE = "VALUE";
                public static final String TYPE = "TYPE";
            }
        }
    }
}
