import './App.css';
import {useEffect, useState} from "react";
import {Event, Summary, service} from "./service"
import { CreateEvent } from './subPages/CreateEvent';
import { SubmitFeedback } from './subPages/SubmitFeedback';
import { EventSummary } from './subPages/EventSummary';

function App() {

    const [showCreateEvent, setShowCreateEvent] = useState(false);
    const [showEventSummary, setShowEventSummary] = useState(false);
    const [showSubmitFeedback, setShowSubmitFeedback] = useState(false);

    const [title, setTitle] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [summary, setSummary] = useState<Summary>();
    const [feedback, setFeedback] = useState<string>('');

    const [events, setEvents] = useState<Event[]>([]);
    const [selectedEvent, setSelectedEvent] = useState<Event>();

    const loadEvents = async () => {
        const eventData = await service.getEvents();
        setEvents(eventData);
    }

    useEffect(() => {
        loadEvents();
    }, []);

    const handleCreateEvent = async () => {
        await service.createEvent(title, description);
        setShowCreateEvent(false)
        setTitle('');
        setDescription('');
        await loadEvents();
    }

    const handleSubmitFeedback = async () => {

        if(!selectedEvent) return; // satisfy compiler
        await service.submitFeedback(selectedEvent.id, feedback);
        setShowSubmitFeedback(false)
        setSelectedEvent(undefined);
        setFeedback('');
    }

    const handleShowSummary = async (event: Event) => {
        setSelectedEvent(event);
        const data = await service.getSummary(event.id);
        setSummary(data);
        setShowEventSummary(true);
    }

  return (
    <div className="App">
      {showCreateEvent ? (
        <CreateEvent
          title={title}
          description={description}
          setTitle={setTitle}
          setDescription={setDescription}
          onCancel={() => setShowCreateEvent(false)}
          onConfirm={handleCreateEvent}
        />
      ) : showSubmitFeedback ? (
        <SubmitFeedback
          setFeedback={setFeedback}
          onCancel={() => setShowSubmitFeedback(false)}
          onConfirm={handleSubmitFeedback}
        />
      ) : showEventSummary ? (
        <EventSummary
          event={selectedEvent!}
          summary={summary!}
          onCancel={() => setShowEventSummary(false)}
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
                                  handleShowSummary(event);
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