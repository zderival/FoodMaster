package com.zderival.FoodMaster.llm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiCandidate {
    GeminiContent content;
    String finishReason;
}
