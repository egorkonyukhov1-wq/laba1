package konukhov.laba1.model;

public record RefreshResult(TokenPair pair, RefreshFailure failure) {
    public static RefreshResult success(TokenPair p) { return new RefreshResult(p, null); }
    public static RefreshResult failure(RefreshFailure f) { return new RefreshResult(null, f); }
    public boolean isSuccess() { return pair != null; }
}