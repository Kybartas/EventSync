import './App.css';
import {useEffect, useState} from "react";
import {Event, Summary, service} from "./service"

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

        if(!selectedEvent) return; // satisfy compiler
        const data = await service.getSummary(selectedEvent.id);
        setSummary(data);
        setShowEventSummary(true);
    }

  return (
    <div className="App">

        <div className="component">

            <h1> Events </h1>
            <button className="button" onClick={() => setShowCreateEvent(true)}>
                Create Event
            </button>

            <div className="event-list">
                {events.length === 0 ? (<p>No events yet.</p>) : (

                    events.map((event) => (
                        <div className="event-item" key={event.id}>
                            <h3>{event.title}</h3>
                            <p>{event.description}</p>
                            <button className="button" onClick={() => {
                                setSelectedEvent(event);
                                setShowSubmitFeedback(true);
                            }}>
                                Submit feedback
                            </button>
                            <button className="button" onClick={() => {handleShowSummary(event);}}>
                                View summary
                            </button>
                        </div>
                    ))
                )}
            </div>
        </div>

        {showCreateEvent && (
            <div className="modal-overlay">

                <div className="modal-content">

                    <h1>Create event</h1>

                    <label>
                    Title
                        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)}/>
                    </label>

                    <label>
                        Description
                        <input type="text" value={description} onChange={(e) => setDescription(e.target.value)}/>
                    </label>

                    <div className="modal-buttons">
                        <button className="button" onClick={() => setShowCreateEvent(false)}>
                            Cancel
                        </button>

                        <button className="button" onClick={handleCreateEvent}>
                            Confirm
                        </button>
                    </div>

                </div>
            </div>
        )}

        {showSubmitFeedback && (
            <div className="modal-overlay">
                <div className="modal-content">

                    <h1>Submit feedback</h1>

                    <label>
                        <input type="text" onChange={(e) => setFeedback(e.target.value)}/>
                    </label>

                    <div className="modal-buttons">
                        <button className="button" onClick={() => setShowSubmitFeedback(false)}>
                            Cancel
                        </button>
                        <button className="button" onClick={handleSubmitFeedback}>
                            Confirm
                        </button>

                    </div>
                </div>
            </div>
        )}

        {showEventSummary && (
            <div className="modal-overlay">
                <div className="modal-content">

                    <h1>{selectedEvent?.title} summary</h1>

                    {summary ? (
                        <div>
                            <p>{summary.count} feedback submissions</p>
                            <ul>
                                {summary.sentiments.map((sentiment, index) => (
                                    <li key={index}>{sentiment}</li>
                                ))}
                            </ul>
                        </div>
                    ) : (
                        <p>Loading summary...</p>
                    )}

                    <div className="modal-buttons">
                        <button className="button" onClick={() => setShowEventSummary(false)}>
                            OK
                        </button>

                    </div>
                </div>
            </div>
        )}

    </div>
  );
}

export default App;