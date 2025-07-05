import './App.css';
import {useEffect, useState} from "react";
import {Event, service} from "./service"
import { CreateEvent } from './subPages/CreateEvent';
import { SubmitFeedback } from './subPages/SubmitFeedback';
import { EventSummary } from './subPages/EventSummary';

function App() {

    const [showCreateEvent, setShowCreateEvent] = useState(false);
    const [showEventSummary, setShowEventSummary] = useState(false);
    const [showSubmitFeedback, setShowSubmitFeedback] = useState(false);

    const [events, setEvents] = useState<Event[]>([]);
    const [selectedEvent, setSelectedEvent] = useState<Event>();

    const loadEvents = async () => {
        const eventData = await service.getEvents();
        setEvents(eventData);
    }

    useEffect(() => {
        loadEvents();
    }, []);

  return (
    <div className="App">

      {showCreateEvent ? (
        <CreateEvent
            close={() => {setShowCreateEvent(false); loadEvents();}}
        />
      ) : showSubmitFeedback ? (
        <SubmitFeedback
            event={selectedEvent!}
            close={() => setShowSubmitFeedback(false)}
        />
      ) : showEventSummary ? (
        <EventSummary
            event={selectedEvent!}
            close={() => setShowEventSummary(false)}
        />
      ) : (
        <div className="component">
            <h1> Events </h1>

            <button className="button" onClick={() => setShowCreateEvent(true)}>
                Create Event
            </button>

            <div className="event-list">
                {events.length === 0 ? (<p>No events yet.</p>) : (

                    events.map((event) => (
                        <div className="event-item" key={event.id}>
                            <h1>{event.title}</h1>
                            <p>{event.description}</p>

                            <div className="bottom-buttons">
                                <button className="button" onClick={() => {
                                    setSelectedEvent(event);
                                    setShowSubmitFeedback(true);
                                }}>
                                    Submit feedback
                                </button>
                                <button className="button" onClick={() => {
                                    setSelectedEvent(event);
                                    setShowEventSummary(true);
                                }}>
                                    View summary
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
      )}
    </div>
  );
}

export default App;