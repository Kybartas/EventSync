type Props = {
    setFeedback: (feedback: string) => void;
    onCancel: () => void;
    onConfirm: () => void;
}

export function SubmitFeedback(props: Props) {
    return (
        <div className="component">

            <h1>Submit feedback</h1>

            <textarea onChange={(e) => props.setFeedback(e.target.value)}/>

            <div className="bottom-buttons">
                <button className="button" onClick={props.onCancel}>
                    Cancel
                </button>
                <button className="button" onClick={props.onConfirm}>
                    Confirm
                </button>
            </div>
        </div>
    )
}