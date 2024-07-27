package cs2024.inf.CineView.dto.reports;

enum ViolationType {
    HATE, // slurs, racism, dehumanization, hateful symbols etc.
    ABUSE_AND_HARASSMENT,
    VIOLENT_SPEECH,
    PRIVACY,
    CHILD_SAFETY,
    SPAM, // fake engagement, scams, fake accounts, malicious links
    SUICIDE_OR_SELF_HARM,
    IMPERSONATION, // pretending to be someone else
    VIOLENT_AND_HATEFUL_ENTITIES, // violent extremism, terrorism, hate groups & networks
    OTHER
}