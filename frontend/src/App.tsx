import {useState} from "react";
import {Event} from "./service"
import './App.css';
import {CreateEvent} from './views/CreateEvent';
import {SubmitFeedback} from './views/SubmitFeedback';
import {EventSummary} from './views/EventSummary';
import {EventList} from "./views/EventList";

function App() {

    const [view, setView] = useState<"list" | "create" | "feedback" | "summary">("list")
    const [selectedEvent, setSelectedEvent] = useState<Event>();

    return (
        <div className="App">
            {view === "list" && (
                <EventList
                    onCreate={() => setView("create")}
                    onSubmitFeedback={(event) => {
                        setSelectedEvent(event);
                        setView("feedback");
                    }}
                    onShowSummary={(event) => {
                        setSelectedEvent(event);
                        setView("summary");
                    }}
                />
            )}

            {view === "create" && (
                <CreateEvent
                    onClose={() => setView("list")}
                />
            )}

            {view === "feedback" && selectedEvent && (
                <SubmitFeedback
                    event={selectedEvent}
                    onClose={() => setView("list")}
                />
            )}

            {view === "summary" && selectedEvent && (
                <EventSummary
                    event={selectedEvent}
                    onClose={() => setView("list")}
                />
            )}
        </div>
    );
}

export default App;