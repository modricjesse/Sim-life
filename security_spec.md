# Security Specification for Lion Den Sim

## 1. Data Invariants
- All documents under `/users/{userId}/careers/{careerId}` and `/users/{userId}/hall_of_fame/{recordId}` belong strictly to the authenticated user whose UID matches `{userId}` and `data.userId`.
- Document IDs (`userId`, `careerId`, `recordId`) must match `^[a-zA-Z0-9_\-]+$` and be at most 128 characters.
- `createdAt` and `updatedAt` must be valid Firestore `timestamp` values (`<= request.time`), and `createdAt` and `userId` are immutable on update.
- Bounded arrays (`inventory`, `relationships`, `recentLogs`, `badges`) must not exceed their maximum size (25 or 30) and must contain strings when non-empty.

## 2. The Dirty Dozen Payloads
1. Unauthenticated read/write to `/users/alice_123/careers/c1`.
2. Cross-user read/write by `bob_456` to `/users/alice_123/careers/c1`.
3. Identity spoofing: `alice_123` writes a career document with `userId: "bob_456"`.
4. Shadow update: `alice_123` updates a career with an undeclared field `isAdmin: true`.
5. Value poisoning: `alice_123` writes `cgpa: 9.5` (exceeds 5.0 bound) or `energy: 500`.
6. Oversized string attack: `studentName` with 5,000 characters.
7. Unbounded array attack: `recentLogs` with 100 items.
8. Immutable field tampering: changing `createdAt` or `userId` during an `update`.
9. Future timestamp injection: `createdAt` set in the future (`> request.time`).
10. Missing required keys on `create`.
11. Invalid `campus` enum value (e.g., `"Lagos"` instead of `"Nsukka"` or `"Enugu"`).
12. Cross-user list query on `/users/alice_123/hall_of_fame` by `bob_456`.
