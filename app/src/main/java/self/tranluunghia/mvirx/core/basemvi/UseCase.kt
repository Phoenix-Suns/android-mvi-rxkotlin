package self.tranluunghia.mvirx.core.basemvi

interface UseCase<in PARAMS, out TYPE> {
    operator fun invoke(params: PARAMS): TYPE
}