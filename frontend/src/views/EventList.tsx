import {useEffect, useState} from "react";
import {Event, service} from "../service";

type Props = {
    onCreate: () => void;
    onSubmitFeedback: (event: Event) => void;
    onShowSummary: (event: Event) => void;
}

export function EventList(props: Props) {

    const [events, setEvents] = useState<Event[]>([])

    const loadEvents = async () => {
        const eventList = await service.getEvents();
        setEvents(eventList);
    }

    useEffect(() => {
        loadEvents();
    }, []);

    return (
        <div className="component">
            <h1> Events </h1>

            <button className="button" onClick={() => props.onCreate()}>
                Create Event
            </button>

            <div className="event-list">
                {events.length === 0 ? (<p>No events yet.</p>) : (

                    events.map((event) => (

                        <div className="event-item" key={event.id}>
                            <h1>{event.title}</h1>
                            <p>{event.description}</p>

                            <div className="bottom-buttons">
                                <button className="button" onClick={() => {props.onSubmitFeedback(event)}}>
                                    Submit feedback
                                </button>
                                <button className="button" onClick={() => {props.onShowSummary(event)}}>
                                    View summary
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    )
}