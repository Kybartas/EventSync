export const API_URL = process.env.REACT_APP_API_URL;

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

        await fetch(API_URL + "/events", {
            method: "POST",
            headers: { "Content-type": "application/json"},
            body: JSON.stringify(body)
        });
    },

    getEvents: async (): Promise<Event[]> => {

        const response = await fetch(API_URL + "/events");
        return await response.json();
    },

    submitFeedback: async (eventId: number, feedback: string) => {

        await fetch(API_URL + `/events/${eventId}/feedback`, {
            method: "Post",
            headers: { "Content-type": "text/plain"},
            body: feedback
        });
    },

    getSummary: async (eventId: number): Promise<Summary> => {

        const response = await fetch(API_URL + `/events/${eventId}/summary`);
        return await response.json();
    }
}