package com.osrschatcompanion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

class ChatCompanionPanel extends PluginPanel
{
	private final JPanel messageList = new JPanel();
	private final JScrollPane scrollPane;
	private final JTextField input = new JTextField();

	private Consumer<String> onSubmit;

	@Inject
	ChatCompanionPanel()
	{
		super(false);
		setLayout(new BorderLayout(0, 8));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JLabel title = new JLabel("Chat Companion");
		title.setForeground(ColorScheme.BRAND_ORANGE);
		add(title, BorderLayout.NORTH);

		messageList.setLayout(new BoxLayout(messageList, BoxLayout.Y_AXIS));
		messageList.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		scrollPane = new JScrollPane(messageList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
		add(scrollPane, BorderLayout.CENTER);

		input.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		input.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		input.setCaretColor(ColorScheme.LIGHT_GRAY_COLOR);
		input.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					submit();
				}
			}
		});
		add(input, BorderLayout.SOUTH);

		addMessageBubble("You", "Phase 1: type a message and press Enter. "
			+ "The message and a JSON dump of local player state are written to the client log.");
	}

	void init(Consumer<String> onSubmit)
	{
		this.onSubmit = onSubmit;
	}

	void addSystemMessage(String text)
	{
		SwingUtilities.invokeLater(() -> addMessageBubble("System", text));
	}

	private void submit()
	{
		String text = input.getText();
		if (text == null)
		{
			return;
		}
		text = text.trim();
		if (text.isEmpty() || onSubmit == null)
		{
			return;
		}

		addMessageBubble("You", text);
		input.setText("");
		onSubmit.accept(text);
	}

	private void addMessageBubble(String who, String text)
	{
		JPanel bubble = new JPanel();
		bubble.setLayout(new BorderLayout());
		bubble.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		bubble.setBorder(new EmptyBorder(6, 8, 6, 8));
		bubble.setAlignmentX(LEFT_ALIGNMENT);

		JLabel whoLabel = new JLabel(who);
		whoLabel.setForeground(ColorScheme.BRAND_ORANGE);
		bubble.add(whoLabel, BorderLayout.NORTH);

		JLabel body = new JLabel("<html><body style='width:180px'>" + escapeHtml(text) + "</body></html>");
		body.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		bubble.add(body, BorderLayout.CENTER);

		messageList.add(bubble);
		messageList.add(Box.createVerticalStrut(6));
		messageList.revalidate();
		messageList.repaint();

		SwingUtilities.invokeLater(() ->
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
	}

	private static String escapeHtml(String text)
	{
		return text
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;");
	}
}
