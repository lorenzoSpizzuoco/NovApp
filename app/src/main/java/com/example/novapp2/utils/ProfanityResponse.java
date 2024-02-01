package com.example.novapp2.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfanityResponse {
    @SerializedName("attributeScores")
    private AttributeScores attributeScores;

    @SerializedName("languages")
    private List<String> languages;

    @SerializedName("detectedLanguages")
    private List<String> detectedLanguages;

    public AttributeScores getAttributeScores() {
        return attributeScores;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getDetectedLanguages() {
        return detectedLanguages;
    }

    // Inner class representing "attributeScores"
    public static class AttributeScores {
        @SerializedName("PROFANITY")
        private AttributeScore profanityScore;

        @SerializedName("TOXICITY")
        private AttributeScore toxicityScore;

        public AttributeScore getProfanityScore() {
            return profanityScore;
        }

        public AttributeScore getToxicityScore() {
            return toxicityScore;
        }
    }

    // Inner class representing "spanScores" and "summaryScore"
    public static class AttributeScore {
        @SerializedName("spanScores")
        private List<SpanScore> spanScores;

        @SerializedName("summaryScore")
        private SummaryScore summaryScore;

        public List<SpanScore> getSpanScores() {
            return spanScores;
        }

        public SummaryScore getSummaryScore() {
            return summaryScore;
        }
    }

    // Inner class representing "spanScores"
    public static class SpanScore {
        private int begin;
        private int end;
        private Score score;

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }

        public Score getScore() {
            return score;
        }
    }

    // Inner class representing "score"
    public static class Score {
        private double value;
        private String type;

        public double getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }

    // Inner class representing "summaryScore"
    public static class SummaryScore {
        private double value;
        private String type;

        public double getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }
}
