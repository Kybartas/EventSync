export interface Event {
    id: number,
    title: string,
    description: string;
}

export interface Summary {
    total: number
    positive: number;
    neutral: number;
    negative: number;
}

export const service = {

    createEvent: async (title: string, description: string) => {

        const body = {
            title: title,
            description: description
        }

        await fetch("http://localhost:8080/eventSync/events", {
            method: "POST",
            headers: { "Content-type": "application/json"},
            body: JSON.stringify(body)
        });
    },

    getEvents: async (): Promise<Event[]> => {

        // GET by default
        const response = await fetch("http://localhost:8080/eventSync/events");
        return await response.json();
    },

    submitFeedback: async (eventId: number, feedback: string) => {

        await fetch(`http://localhost:8080/eventSync/events/${eventId}/feedback`, {
            method: "Post",
            headers: { "Content-type": "application/json"},
            body: feedback
        });
    },

    getSummary: async (eventId: number): Promise<Summary> => {

        const response = await fetch(`http://localhost:8080/eventSync/events/${eventId}/summary`);
        return await response.json();
    }
}