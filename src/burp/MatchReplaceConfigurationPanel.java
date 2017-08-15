package burp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class MatchReplaceConfigurationPanel extends JPanel {
	
	private JTable table;
	private static final int INDEX_COL = 0;
	private static final int MATCH_COL = 1;
	private static final int REPLACE_COL = 2;
	private DefaultTableModel dataModel;
	
	public MatchReplaceConfigurationPanel() {
		setLayout(new BorderLayout(0, 0));
		prepareTable();
		prepareHeader();
		prepareButtons();
	}
	
	public List<MatchReplace> getData() {
		List<MatchReplace> data = new ArrayList<>();
		int rowCount = table.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			data.add(new MatchReplace((String)table.getValueAt(i, MATCH_COL), (String)table.getValueAt(i, REPLACE_COL)));
		}
		return data;
	}
	
	private void prepareTable() {
		table = new JTable();
		
		dataModel = new DefaultTableModel(new Object[][] {}, new String[] {"#", "Match", "Replace"}) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		table.setBounds(2, 50, 315, 32);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setModel(dataModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(400);
		table.getColumnModel().getColumn(1).setMaxWidth(400);
		table.getColumnModel().getColumn(2).setPreferredWidth(300);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private JLabel createLabelURL(String url) {
		JLabel lblUrl = new JLabel(url);
		lblUrl.setForeground(Color.BLUE);
		lblUrl.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(lblUrl.getText()));
				} catch (URISyntaxException | IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		return lblUrl;
	}
	
	private void prepareHeader() {
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		JLabel patternUrl = createLabelURL("https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html");
		JLabel gitHubUrl = createLabelURL("https://github.com/LogicalTrust/BurpSessionHandlingMatchReplace");
		
		JButton btn = new JButton("?");
		btn.addActionListener((ev) -> {
			JOptionPane.showMessageDialog(null, new Object[] { "Pattern syntax based on:", patternUrl, "GitHub:", gitHubUrl });
		});
		
		panel.add(btn);
	}
	
	private void prepareButtons() {
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		prepareAddButton(panel_1);
		prepareEditButton(panel_1);
		prepareRemoveButton(panel_1);
	}

	private void prepareRemoveButton(JPanel panel_1) {
		JButton btnRemove = new JButton("Delete");
		btnRemove.addActionListener((event) -> {
			int selectedRow = table.getSelectedRow();
			dataModel.removeRow(selectedRow);
			int rowCount = dataModel.getRowCount() - selectedRow;
			for (int i = 0; i < rowCount; i++) {
				dataModel.setValueAt(i + selectedRow + 1, i + selectedRow, INDEX_COL);
			}
		});
		panel_1.add(btnRemove);
	}

	private void prepareEditButton(JPanel panel_1) {
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener((event) -> {
			int selectedRow = table.getSelectedRow();
			JTextField match = new JTextField((String)table.getValueAt(selectedRow, MATCH_COL));
			JTextField replace = new JTextField((String)table.getValueAt(selectedRow, REPLACE_COL));
			Object[] message = {
			    "Match:", match,
			    "Replace:", replace
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Edit", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				table.setValueAt(match.getText(), selectedRow, MATCH_COL);
				table.setValueAt(replace.getText(), selectedRow, REPLACE_COL);
			}
		});
		panel_1.add(btnEdit);
	}

	private void prepareAddButton(JPanel panel_1) {
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener((event) -> {
			JTextField match = new JTextField();
			JTextField replace = new JTextField();
			Object[] message = {
			    "Match:", match,
			    "Replace:", replace
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Add", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				int position = dataModel.getRowCount() + 1;
				dataModel.addRow(new Object[] { position, match.getText(), replace.getText() });
			}
		});
		panel_1.add(btnAdd);
	}
	
}