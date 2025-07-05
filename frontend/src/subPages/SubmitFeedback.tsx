import {useState} from "react";
import {Event, service} from "../service";

type Props = {
    event: Event;
    close: () => void;
}

export function SubmitFeedback(props: Props) {

    const [feedback, setFeedback] = useState<string>('');

    const handleSubmitFeedback = async () => {

        await service.submitFeedback(props.event.id, feedback);
        props.close();
    }

    return (
        <div className="component">
            <h1>Submit feedback</h1>

            <textarea value={feedback} onChange={(e) => setFeedback(e.target.value)}/>

            <div className="bottom-buttons">
                <button className="button" onClick={props.close}>
                    Cancel
                </button>
                <button className="button" onClick={handleSubmitFeedback}>
                    Confirm
                </button>
            </div>
        </div>
    )
}