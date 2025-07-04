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
                <h2>Total submissions: {props.summary.total}</h2>
                <h2 className="positive"> Positive submissions: {props.summary.positive}</h2>
                <h2 className="negative">Negative submissions: {props.summary.negative}</h2>
                <h2> Neutral submissions: {props.summary.neutral}</h2>

            </div>

            <div className="bottom-buttons">
                <button className="button" onClick={props.onCancel}>
                    Cancel
                </button>
            </div>
        </div>
    )
}