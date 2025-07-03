import { Event, Summary } from "../service";

type Props = {
    event: Event;
    summary: Summary;
    onCancel: () => void;
}

export function EventSummary(props: Props) {
    return (
        <div className="component">

            <h1>{props.event.title} summary</h1>

            <div>
                <h2>Total submissions: {props.summary.count}</h2>
                <ul>
                    {props.summary.sentiments.map((sentiment, index) => (
                        <li key={index}>{sentiment}</li>
                    ))}
                </ul>
            </div>

            <div className="bottom-buttons">
                <button className="button" onClick={props.onCancel}>
                    Cancel
                </button>
            </div>
        </div>
    )
}