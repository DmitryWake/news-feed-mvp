package ru.newsfeedmvp.features.nlp.tonepredictor

import com.londogard.nlp.meachinelearning.predictors.transformers.ClassifierPipeline
import com.londogard.nlp.utils.huggingface.Engine
import ru.newsfeedmvp.features.nlp.tonepredictor.model.ReactionType

class LocalTonePredictor: TonePredictor {

    //FIXME Проблема, что библиотека работает только с TorchScript моделями
    private val pipeline by lazy {
        ClassifierPipeline.create("cointegrated/rubert-tiny2-cedr-emotion-detection", Engine.PYTORCH)
    }

    override suspend fun checkTone(data: List<String>): ReactionType {
        pipeline.predict("test")
        return ReactionType.NO_EMOTION
    }
}